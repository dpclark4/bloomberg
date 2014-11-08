//comment change22

    import com.bloomberglp.blpapi.*;

public class RequestResponseParadigm {
        public static void main(String[] args) throws Exception {
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
            request.set("security", "DJI Index");
            request.set("eventType", "TRADE");
            request.set("interval", 60); // bar interval in minutes
            request.set("startDateTime", new Datetime(2014, 9, 26, 13, 30, 0, 0));
            request.set("endDateTime", new Datetime(2014, 9, 26, 21, 30, 0, 0));            session.sendRequest(request, requestID);
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
        private static void handleResponseEvent(Event event) throws Exception { System.out.println("EventType =" + event.eventType()); MessageIterator iter = event.messageIterator();
            while (iter.hasNext()) {
                Message message = iter.next();
                System.out.println("correlationID=" +
                        message.correlationID());
                System.out.println("messageType  =" +
                        message.messageType());
                message.print(System.out);
            } }
        private static void handleOtherEvent(Event event) throws Exception
        {
            System.out.println("EventType=" + event.eventType());
            MessageIterator iter = event.messageIterator();
            while (iter.hasNext()) {
                Message message = iter.next();
                System.out.println("correlationID=" +
                        message.correlationID());
                System.out.println("messageType=" + message.messageType());
                message.print(System.out);
                if (Event.EventType.Constants.SESSION_STATUS ==
                        event.eventType().intValue()
                        &&  "SessionTerminated".equals(
                        message.messageType().toString())){
                    System.out.println("Terminating: " +    message.messageType());
                            System.exit(1);
                }
            } }
    }

