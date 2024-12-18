package services;

/**
 * Generic Response class to encapsulate responses from service methods.
 *
 * @param <T> The type of data being returned.
 */
public class Response<T> {

    // Response structure consists of message, success flag, and data.
    private String message;
    private boolean success;
    private T data;

    /**
     * Default constructor.
     */
    public Response() {
    }

    /**
     * Gets the response message.
     *
     * @return The response message.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the response message.
     *
     * @param message The response message.
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Checks if the response indicates success.
     *
     * @return True if successful, else false.
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Sets the success flag of the response.
     *
     * @param success The success flag.
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }

    /**
     * Gets the data associated with the response.
     *
     * @return The response data.
     */
    public T getData() {
        return data;
    }

    /**
     * Sets the data associated with the response.
     *
     * @param data The response data.
     */
    public void setData(T data) {
        this.data = data;
    }
}
