mport java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.TimeUnit;

public class FlowApiExample {
    public static void main(String[] args) throws InterruptedException {
        // Create a SubmissionPublisher
        SubmissionPublisher<Integer> publisher = new SubmissionPublisher<>();

        // Create a custom Processor
        MySubscriber subscriber = new MySubscriber();

        // Subscribe the Subscriber to the Publisher
        publisher.subscribe(subscriber);
        TimeUnit.SECONDS.sleep(1);
        // Publish some items to the Publisher
        for (int i = 1; i <= 5; i++) {
            System.out.println("Publisher: Emitting item " + i);
            publisher.submit(i);
        }

        // Close the publisher to signal completion
        publisher.close();
    }
}
 42 changes: 42 additions & 0 deletions42  
src/com/itbulls/learnit/javacore/javaupdates/java9/flowapi/MySubscriber.java
Original file line number	Diff line number	Diff line change
@@ -0,0 +1,42 @@
package com.itbulls.learnit.javacore.javaupdates.java9.flowapi;

import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;

//Custom Processor implementing the Subscriber and Publisher interfaces
class MySubscriber implements Subscriber<Integer> {
	private Subscription subscription;

	@Override
	public void onSubscribe(Subscription subscription) {
		System.out.println("Subscriber: Subscribed");
		System.out.println(subscription);
		this.subscription = subscription;
		subscription.request(1); // Request the first item when subscribed
	}

	@Override
	public void onNext(Integer item) {
		System.out.println("Subscriber: Received item " + item);
		// Transform the integer into a string and pass it downstream
		submit(String.valueOf(item * 2));
		subscription.request(1); // Request the next item
	}

	@Override
	public void onError(Throwable throwable) {
		System.err.println("Subscriber: Error - " + throwable.getMessage());
	}

	@Override
	public void onComplete() {
		System.out.println("Subscriber: Completed");
		subscription.cancel();
	}

	// Helper method to submit an item downstream
	private void submit(String item) {
		System.out.println("Subscriber: Emitting item " + item);
	}

}
