package Service;

import DAO.MessageDAO;
import DAO.AccountDAO;
import Model.Message;
import Model.Account;

import java.util.List;

public class MessageService {
    private MessageDAO messageDAO;
    private AccountDAO accountDAO;

    public MessageService() {
        this.messageDAO = new MessageDAO();
        this.accountDAO = new AccountDAO();
    }

    // Renamed to match controller
    public Message createMessage(Message message) {
        if (message.getMessage_text() == null || message.getMessage_text().isBlank() || message.getMessage_text().length() > 255) {
            return null;
        }

        Account account = accountDAO.getAccountById(message.getPosted_by());
        if (account == null) {
            return null;
        }

        return messageDAO.insertMessage(message);
    }

    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    public Message getMessageById(int id) {
        return messageDAO.getMessageById(id);
    }

    public Message deleteMessage(int id) {
        Message existing = messageDAO.getMessageById(id);
        if (existing == null) {
            return null;
        }
        boolean deleted = messageDAO.deleteMessage(id);
        return deleted ? existing : null;
    }

    public Message updateMessage(int id, String newText) {
        if (newText == null || newText.isBlank() || newText.length() > 255) {
            return null;
        }

        Message existing = messageDAO.getMessageById(id);
        if (existing == null) {
            return null;
        }

        existing.setMessage_text(newText);
        boolean updated = messageDAO.updateMessage(existing);
        return updated ? existing : null;
    }

    public List<Message> getMessagesByUser(int userId) {
        return messageDAO.getMessagesByUserId(userId);
    }
}
