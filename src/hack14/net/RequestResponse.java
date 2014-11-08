package hack14.net;

/**
 * Created by bean on 11/8/2014.
 */

import com.bloomberglp.blpapi.*;

import java.util.ArrayList;

public class RequestResponse {
    private ArrayList<Message> messageList;

    public RequestResponse() throws Exception {
        messageList = new ArrayList<Message>();
    }
    public Message getMessage(){

        return messageList.get(0);
    }
    public void getSecurityData(String security, int month, int day )throws Exception {
        SessionOptions sessionOptions = new SessionOptions();
        sessionOptions.setServerHost("10.8.8.1");
        sessionOptions.setServerPort(8194);
        Session session = new Session(sessionOptions);
        if (!session.start()) {
            System.out.println("Could not start session.");
            System.exit(1);
        }
        if (!session.openService("//blp/refdata")) {
            System.out.println("Could not open service " +
                    "//blp/refdata");
            System.exit(1);
        }
        CorrelationID requestID  = new CorrelationID(1);
        Service refDataService = session.getService("//blp/refdata");
        Request request = refDataService.createRequest("IntradayBarRequest");
        request.set("security", security);
        request.set("eventType", "TRADE");
        request.set("interval", 60); // bar interval in minutes
        request.set("startDateTime", new Datetime(2014, month, day, 12, 0, 0, 0));
        request.set("endDateTime", new Datetime(2014, month, day, 23, 0, 0, 0));
        session.sendRequest(request, requestID);
        boolean continueToLoop = true;
        while (continueToLoop) {
            Event event = session.nextEvent();
            switch (event.eventType().intValue()) {
                case Event.EventType.Constants.RESPONSE: // final event
                    continueToLoop = false;               // fall through
                case Event.EventType.Constants.PARTIAL_RESPONSE:
                    handleResponseEvent(event);
                    break;
                default:
                    handleOtherEvent(event);
                    break;
            } }

    }
    private void handleResponseEvent(Event event) throws Exception { //System.out.println("EventType =" + event.eventType());
     MessageIterator iter = event.messageIterator();
        while (iter.hasNext()) {
            Message message = iter.next();
            messageList.add(message);
            /*
            System.out.println("correlationID=" +
                    message.correlationID());
            System.out.println("messageType  =" +
                    message.messageType());
            message.print(System.out);
            */
        }
    }
    private void handleOtherEvent(Event event) throws Exception
    {
        //System.out.println("EventType=" + event.eventType());
        MessageIterator iter = event.messageIterator();
        while (iter.hasNext()) {
            Message message = iter.next();
            /*
            System.out.println("correlationID=" +
                    message.correlationID());
            System.out.println("messageType=" + message.messageType());
            message.print(System.out);
            */
            if (Event.EventType.Constants.SESSION_STATUS ==
                    event.eventType().intValue()
                    &&  "SessionTerminated".equals(
                    message.messageType().toString())){
                System.out.println("Terminating: " +    message.messageType());
                System.exit(1);
            }
        } }
}

