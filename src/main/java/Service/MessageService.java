package Service;

import DAO.MessageDAO;
import DAO.AccountDAO;
import Model.Message;
import Model.Account;
import java.util.List;

public class MessageService {

    private MessageDAO messageDAO;
    private AccountDAO accountDAO;  // AccountDAO for checking account existence

    // Constructor initializes both DAOs
    public MessageService() {
        this.messageDAO = new MessageDAO();  // Assuming MessageDAO has a no-args constructor
        this.accountDAO = new AccountDAO();  // Assuming AccountDAO has a no-args constructor
    }

    // Optionally, add a constructor for testing or if using dependency injection
    public MessageService(MessageDAO messageDAO, AccountDAO accountDAO) {
        this.messageDAO = messageDAO;
        this.accountDAO = accountDAO;
    }

    public Message createMessage(Message message) {
        if (message.getMessage_text() != null && !message.getMessage_text().isBlank()
            && message.getMessage_text().length() <= 255) {
            // Verify if the user (account) posting the message exists
            Account account = accountDAO.getAccountById(message.getPosted_by());
            if (account != null) {
                return messageDAO.createMessage(message);
            }
        }
        return null;
    }

    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    public Message getMessageById(int messageId) {
        Message message = MessageDAO.getMessageById(messageId);
        if (message != null) {
            return message;  // Return the message if found
        } 
        return null;  // Return null if the message is not found
    }

    public boolean deleteMessageById(int messageId) {
        return messageDAO.deleteMessage(messageId);
    }

    public boolean updateMessage(int messageId, String newText) {
        if (newText != null && !newText.isBlank() && newText.length() <= 255) {
            return messageDAO.updateMessageText(messageId, newText);
        }
        return false;
    }

    public List<Message> getMessagesByUser(int accountId) {
        return messageDAO.getMessagesByUser(accountId);
    }
}
