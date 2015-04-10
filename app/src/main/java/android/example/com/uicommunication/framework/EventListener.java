package android.example.com.uicommunication.framework;

/**
 * A simple event listener that receives events of a specific type.
 * @param <T> The type of event received by this event listener.
*/
public interface EventListener<T> {

	/**
	 * Called when the event arrives.
	 */
	void onEvent(T event);
}