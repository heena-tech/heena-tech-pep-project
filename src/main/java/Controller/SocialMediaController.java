package Controller;
import Model.Message;
import Model.Account;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;
import java.util.List;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
      private AccountService accountService = new AccountService();
      private MessageService messageService = new MessageService();
    
   public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);

        // account routes
        app.post("/register",this::handleRegister);
        app.post("/login",this::handleLogin);

        // message 
        app.post("/messages",this::handleCreateMessage);
        app.get("/messages",this:: handleGetAllMessages);
        app.get("/messages/{message_id}",this::handleGetMessageById);
        app.delete("/messages/{message_id}",this::handleDeleteMessage);
        app.patch("/messages/{message_id}",this::handleUpdateMessage);
        app.get("accounts/{account_id}/messages",this::handleGetMessagesByUserId);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    private void handleRegister(Context context){
       Account input = context.bodyAsClass(Account.class);

       if (input.getUsername() == null || input.getUsername().isBlank() || input.getPassword().length() < 4) {
            context.status(400);
            return;
    }
    Account created = accountService.register(input);
        if (created == null) {
            context.status(400);
        } else {
            context.status(200).json(created);
        }
    }

   private void handleLogin(Context context) {
        Account input = context.bodyAsClass(Account.class);
        Account result = accountService.login(input.getUsername(), input.getPassword());
        if (result == null) {
            context.status(401);
        } else {
            context.json(result);
        }
    }
  
     private void handleCreateMessage(Context context) {
        Message msg = context.bodyAsClass(Message.class);
        if (msg.getMessage_text() == null || msg.getMessage_text().isBlank() || msg.getMessage_text().length() > 255) {
            context.status(400);
            return;
        }
        Message created = messageService.createMessage(msg);
        if (created == null) {
            context.status(400);
        } else {
            context.status(200).json(created);
        }
    }

     private void handleGetAllMessages(Context context) {
        List<Message> messages = messageService.getAllMessages();
        context.json(messages);
    }

    private void handleGetMessageById(Context context) {
        int id = Integer.parseInt(context.pathParam("message_id"));
        Message msg = messageService.getMessageById(id);
        if (msg != null) {
            context.json(msg);
        } else {
            context.status(200); // Test expects 200 with empty body
        }
    }

     private void handleDeleteMessage(Context context) {
        int id = Integer.parseInt(context.pathParam("message_id"));
        Message deleted = messageService.deleteMessage(id);
        if (deleted != null) {
            context.json(deleted);
        } else {
            context.status(200); // still 200 with empty body
        }
    }

      private void handleUpdateMessage(Context context) {
        int id = Integer.parseInt(context.pathParam("message_id"));
        Message update = context.bodyAsClass(Message.class);
        if (update.getMessage_text() == null || update.getMessage_text().isBlank() || update.getMessage_text().length() > 255) {
            context.status(400);
            return;
        }
        Message updated = messageService.updateMessage(id, update.getMessage_text());
        if (updated != null) {
            context.json(updated);
        } else {
            context.status(400);
        }
    }

     private void handleGetMessagesByUserId(Context context) {
        int userId = Integer.parseInt(context.pathParam("account_id"));
        List<Message> messages = messageService.getMessagesByUser(userId);
        context.json(messages);
    }


}