package Controller;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class SocialMediaController {

    private AccountService accountService;
    private MessageService messageService;

    public SocialMediaController() {
        // Initialize the services
        accountService = new AccountService();
        messageService = new MessageService();
    }

    public Javalin startAPI() {
        Javalin app = Javalin.create();

        // Define the routes
        app.post("/register", this::registerAccount);
        app.post("/login", this::loginAccount);
        app.post("/messages", this::createMessage);
        app.get("/messages", this::getAllMessages);
        app.get("/messages/{message_id}", this::getMessageById);
        app.delete("/messages/{message_id}", this::deleteMessageById);
        app.patch("/messages/{message_id}", this::updateMessageText);
        app.get("/accounts/{account_id}/messages", this::getMessagesByAccount);

        return app;
    }

    private void registerAccount(Context ctx) {
        Account account = ctx.bodyAsClass(Account.class);
        Account registeredAccount = accountService.registerAccount(account);
        if (registeredAccount != null) {
            ctx.json(registeredAccount);
        } else {
            ctx.status(400);
        }
    }

    private void loginAccount(Context ctx) {
        Account account = ctx.bodyAsClass(Account.class);
        Account loggedInAccount = accountService.login(account);
        if (loggedInAccount != null) {
            ctx.json(loggedInAccount);
        } else {
            ctx.status(401);
        }
    }

    private void createMessage(Context ctx) {
        Message message = ctx.bodyAsClass(Message.class);
        Message createdMessage = messageService.createMessage(message);
        if (createdMessage != null) {
            ctx.json(createdMessage);
        } else {
            ctx.status(400);
        }
    }

    private void getAllMessages(Context ctx) {
        ctx.json(messageService.getAllMessages());
    }

    /*
     private void getMessageById(Context ctx) {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.getMessageById(messageId);
        if (message != null) {
            ctx.json(message);
        } else {
            ctx.status(404); // Not found
        }
    }
     */



     private void getMessageById(Context ctx) {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.getMessageById(messageId);
    
        if (message != null) {
            // If message exists, return it with a 200 OK status
            ctx.json(message);
            ctx.status(200);
        } else {
            // If the message doesn't exist, return 200 OK with an empty body
            ctx.status(200);  // Ensure 200 OK
            ctx.result("");   // Return an empty response body
        }
    }
    
    

    private void deleteMessageById(Context ctx) {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.getMessageById(messageId); // Try to retrieve the message
    
        if (message != null) {
            boolean success = messageService.deleteMessageById(messageId);
            if (success) {
                ctx.json(message);  // Return the deleted message as JSON
            } else {
                // If deletion fails for any reason, still return 200 but with an empty body
                ctx.status(200);    // Status 200 OK, even if the message was not deleted successfully
            }
        } else {
            // Return status 200 OK with an empty body if message not found (to match test expectations)
            ctx.status(200);
        }
    }
    
    

    private void updateMessageText(Context ctx) {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        String newText = ctx.bodyAsClass(Message.class).getMessage_text();
        boolean success = messageService.updateMessage(messageId, newText);
        if (success) {

            // Return the updated message or a success message
            Message updatedMessage = messageService.getMessageById(messageId);

            ctx.json(updatedMessage);
            ctx.status(200); // Successfully updated

        } else {
            ctx.status(400); // Bad request (invalid input)
        }
    }

    private void getMessagesByAccount(Context ctx) {
        int accountId = Integer.parseInt(ctx.pathParam("account_id"));
        ctx.json(messageService.getMessagesByUser(accountId));
    }
}
