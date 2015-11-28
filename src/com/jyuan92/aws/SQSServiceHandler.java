package com.jyuan92.aws;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.Map.Entry;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.DeleteQueueRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.jyuan92.sentiment.ThreadPool;

public class SQSServiceHandler {

	private static final String QUEUE_NAME = "cloud_computing_sqs";
	private static final int PRODUCE_TASK_MAX_NUMBER = 3;

	private static class Holder {
		private static final SQSServiceHandler sqsService = new SQSServiceHandler();
	}

	public static SQSServiceHandler getInstance() {
		return Holder.sqsService;
	}

	private final AmazonSQS amazonSQS;
	private final ThreadPoolExecutor threadPoolExecutor;
	private String queueUrl;

	private SQSServiceHandler() {
		amazonSQS = new AmazonSQSClient(
				new ClasspathPropertiesFileCredentialsProvider("././AwsCredentials.properties"));
		amazonSQS.setRegion(Region.getRegion(Regions.US_EAST_1));
		try {
			System.out.printf("Creating a new SQS queue called: %s \n", QUEUE_NAME);
			queueUrl = amazonSQS.createQueue(new CreateQueueRequest(QUEUE_NAME)).getQueueUrl();
		} catch (AmazonServiceException ase) {
			printAmazonServiceException(ase);
		} catch (AmazonClientException ace) {
			printAmazonClientException(ace);
		}
		threadPoolExecutor = new ThreadPoolExecutor(2, 4, 3, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(3),
				new ThreadPoolExecutor.DiscardOldestPolicy());
		for (int i = 1; i <= PRODUCE_TASK_MAX_NUMBER; i++) {
			threadPoolExecutor.execute(new ThreadPool());
		}
	}

	public boolean sendMessage(String message) {
		// Send a message
		System.out.println("Sending message to SQS: " + message);
		try {
			amazonSQS.sendMessage(new SendMessageRequest(queueUrl, message));
			return true;
		} catch (AmazonServiceException ase) {
			return printAmazonServiceException(ase);
		} catch (AmazonClientException ace) {
			return printAmazonClientException(ace);
		}
	}

	public String getMessage() {
		String msg = null;
		try {
			// Receive messages
			System.out.println("Receiving messages from SQS.\n");
			List<Message> messages = amazonSQS
					.receiveMessage(new ReceiveMessageRequest(queueUrl).withMaxNumberOfMessages(1)).getMessages();
			if (!messages.isEmpty()) {
				for (Message message : messages) {
					System.out.println("  Message");
					System.out.println("    MessageId:     " + message.getMessageId());
					System.out.println("    ReceiptHandle: " + message.getReceiptHandle());
					System.out.println("    MD5OfBody:     " + message.getMD5OfBody());
					System.out.println("    Body:          " + message.getBody());
					for (Entry<String, String> entry : message.getAttributes().entrySet()) {
						System.out.println("  Attribute");
						System.out.println("    Name:  " + entry.getKey());
						System.out.println("    Value: " + entry.getValue());
					}
				}
				msg = messages.get(0).getBody().toString();
				// Delete a message
				System.out.println("Deleting a message.\n");
				String messageRecieptHandle = messages.get(0).getReceiptHandle();
				amazonSQS.deleteMessage(new DeleteMessageRequest(queueUrl, messageRecieptHandle));
				System.out.println(msg);
			}
		} catch (AmazonServiceException ase) {
			printAmazonServiceException(ase);
		} catch (AmazonClientException ace) {
			printAmazonClientException(ace);
		}
		return msg;
	}

	public boolean DeleteQueueService() {
		try {
			System.out.println("Deleting queue.\n");
			amazonSQS.deleteQueue(new DeleteQueueRequest(queueUrl));
			return true;
		} catch (AmazonServiceException ase) {
			return printAmazonServiceException(ase);
		} catch (AmazonClientException ace) {
			return printAmazonClientException(ace);
		} finally {
			threadPoolExecutor.shutdown();
		}
	}

	private boolean printAmazonServiceException(AmazonServiceException ase) {
		System.out.println("Caught an AmazonServiceException, which means your request made it "
				+ "to Amazon SQS, but was rejected with an error response for some reason.");
		System.out.println("Error Message:    " + ase.getMessage());
		System.out.println("HTTP Status Code: " + ase.getStatusCode());
		System.out.println("AWS Error Code:   " + ase.getErrorCode());
		System.out.println("Error Type:       " + ase.getErrorType());
		System.out.println("Request ID:       " + ase.getRequestId());
		return false;
	}

	private boolean printAmazonClientException(AmazonClientException ace) {
		System.out.println("Caught an AmazonClientException, which means the client encountered "
				+ "a serious internal problem while trying to communicate with SQS, such as not "
				+ "being able to access the network.");
		System.out.println("Error Message: " + ace.getMessage());
		return false;
	}
}