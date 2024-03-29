package com.jyuan92.aws;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.dynamodbv2.datamodeling.KeyPair;
import com.amazonaws.services.elasticbeanstalk.AWSElasticBeanstalkClient;
import com.amazonaws.services.elasticbeanstalk.model.CheckDNSAvailabilityRequest;
import com.amazonaws.services.elasticbeanstalk.model.CheckDNSAvailabilityResult;
import com.amazonaws.services.elasticbeanstalk.model.ConfigurationOptionSetting;
import com.amazonaws.services.elasticbeanstalk.model.CreateApplicationRequest;
import com.amazonaws.services.elasticbeanstalk.model.CreateApplicationVersionRequest;
import com.amazonaws.services.elasticbeanstalk.model.CreateEnvironmentRequest;
import com.amazonaws.services.elasticbeanstalk.model.DescribeApplicationVersionsRequest;
import com.amazonaws.services.elasticbeanstalk.model.DescribeApplicationVersionsResult;
import com.amazonaws.services.elasticbeanstalk.model.DescribeEnvironmentsRequest;
import com.amazonaws.services.elasticbeanstalk.model.DescribeEnvironmentsResult;
import com.amazonaws.services.elasticbeanstalk.model.EnvironmentDescription;
import com.amazonaws.services.elasticbeanstalk.model.S3Location;
import com.amazonaws.services.elasticloadbalancing.AmazonElasticLoadBalancingClient;
import com.amazonaws.services.elasticloadbalancing.model.DescribeLoadBalancersResult;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.PutObjectRequest;

public class Deploy {

	private static final String PROPERTIES_NAME = "AwsCredentials.properties";
	private static final String PROPERTIES_NOT_FOUND = "property file '" + PROPERTIES_NAME
			+ "' not found in the classpath";

	private String accessKey;
	private String secretKey;
	private String KeyPair;

	private void getPropValues() {
		InputStream inputStream = null;
		try {
			Properties prop = new Properties();
			inputStream = getClass().getClassLoader().getResourceAsStream(PROPERTIES_NAME);
			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				throw new FileNotFoundException(PROPERTIES_NOT_FOUND);
			}
			// get the property value
			secretKey = prop.getProperty("secretKey");
			accessKey = prop.getProperty("accessKey");
			KeyPair = prop.getProperty("keyPair");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		Deploy deploy = new Deploy();
		deploy.getPropValues();
		// user credentials
		final String AWS_KEY = deploy.accessKey;
		final String AWS_SECRET_KEY = deploy.secretKey;
		final String KEY_PAIR = deploy.KeyPair;
		
		String sampleApp = "TwitterMap-Sample";
		String versionLabel = "1.0";
		String cnamePrefix = "mysampleapplication-fsdfsd";
		String envName = "TwittAppEn";

		// S3 parameter
		String keyName = "TwitterMap-Sample-KeyName.war";
		String uploadFileName = "TwitterMap-Sample-uploadFileName.war";
		String bucketName = "TwitterMap-Sample-bucketName";
		
		System.out.println("--Reading credential information...");
		AWSCredentials credentials = null;
		try {
			credentials = new ProfileCredentialsProvider("src/credentials", "default").getCredentials();
		} catch (Exception e) {
			throw new AmazonClientException("Cannot load the credentials from the credential profiles file. "
					+ "Please make sure that your credentials file is at the correct "
					+ "location (credentials), and is in valid format.", e);
		}
		System.out.println("--Creating S3 bucket...");
		AmazonS3 s3 = new AmazonS3Client(credentials);
		s3.setEndpoint("s3-us-west-2.amazonaws.com");
		Bucket bucket = s3.createBucket(bucketName);

		System.out.println("--Created S3 bucket.");

		System.out.println("--Uploading files to S3 bucket...");

		try {
			System.out.println("Uploading a new object to S3 from a file\n");
			File file = new File(uploadFileName);
			s3.putObject(new PutObjectRequest(bucketName, keyName, file));
		} catch (AmazonServiceException ase) {
			System.out.println("Caught an AmazonServiceException, which " + "means your request made it "
					+ "to Amazon S3, but was rejected with an error response" + " for some reason.");
			System.out.println("Error Message:    " + ase.getMessage());
		} catch (AmazonClientException ace) {
			System.out.println("Caught an AmazonClientException");
			System.out.println("Error Message: " + ace.getMessage());
		}
		S3Location sourceBundle = new S3Location();
		sourceBundle.withS3Bucket(bucketName);
		sourceBundle.withS3Key(keyName);

		AWSElasticBeanstalkClient awsc = new AWSElasticBeanstalkClient(credentials);
		awsc.setEndpoint("elasticbeanstalk.us-west-2.amazonaws.com");
		AmazonElasticLoadBalancingClient aebc = new AmazonElasticLoadBalancingClient(credentials);

		// To create a new application
		CreateApplicationRequest createApplicationRequest = new CreateApplicationRequest();
		createApplicationRequest.withApplicationName(sampleApp);
		awsc.createApplication(createApplicationRequest);

		CreateApplicationVersionRequest createApplicationVersionRequest = new CreateApplicationVersionRequest();
		createApplicationVersionRequest.withApplicationName(sampleApp).withVersionLabel(versionLabel)
				.withSourceBundle(sourceBundle);
		awsc.createApplicationVersion(createApplicationVersionRequest);

		CheckDNSAvailabilityRequest checkDNSAvailabilityRequest = new CheckDNSAvailabilityRequest();
		checkDNSAvailabilityRequest.withCNAMEPrefix(cnamePrefix);
		CheckDNSAvailabilityResult available = awsc.checkDNSAvailability(checkDNSAvailabilityRequest);
		System.out.println("--DNS is Available?" + available.getAvailable().toString());

		DescribeApplicationVersionsRequest describeApplicationVersionsRequest = new DescribeApplicationVersionsRequest();
		describeApplicationVersionsRequest.setApplicationName(sampleApp);

		ArrayList<String> versionLabels = new ArrayList<String>();
		versionLabels.add(versionLabel);
		describeApplicationVersionsRequest.setVersionLabels(versionLabels);
		DescribeApplicationVersionsResult describeApplicationVersionsResult = awsc
				.describeApplicationVersions(describeApplicationVersionsRequest);
		System.out.println(describeApplicationVersionsResult.getApplicationVersions());

		List<ConfigurationOptionSetting> optionSettings = new ArrayList<ConfigurationOptionSetting>();
		optionSettings
				.add(new ConfigurationOptionSetting("aws:autoscaling:launchconfiguration", "EC2KeyName", KEY_PAIR));
		// set AWS key
		optionSettings.add(new ConfigurationOptionSetting("aws:elasticbeanstalk:application:environment",
				"AWS_ACCESS_KEY_ID", AWS_KEY));
		// set secret key
		optionSettings.add(new ConfigurationOptionSetting("aws:elasticbeanstalk:application:environment",
				"AWS_SECRET_KEY", AWS_SECRET_KEY));
		optionSettings
				.add(new ConfigurationOptionSetting("aws:autoscaling:launchconfiguration", "InstanceType", "t1.micro"));
		optionSettings
				.add(new ConfigurationOptionSetting("aws:autoscaling:launchconfiguration", "ImageId", "ami-2a31bf1a"));// 32bit
																														// Amazon
		optionSettings.add(new ConfigurationOptionSetting("aws:elb:policies", "Stickiness Policy", "true"));
		// set minimum number of instances
		optionSettings.add(new ConfigurationOptionSetting("aws:autoscaling:asg", "MinSize", "1"));
		// set maximum number of instances
		optionSettings.add(new ConfigurationOptionSetting("aws:autoscaling:asg", "MaxSize", "1"));
		CreateEnvironmentRequest createEnvironmentRequest = new CreateEnvironmentRequest();
		createEnvironmentRequest.withApplicationName(sampleApp).withVersionLabel(versionLabel)
				.withEnvironmentName(envName).withSolutionStackName("32bit Amazon Linux running Tomcat 7")
				.withCNAMEPrefix(cnamePrefix).withOptionSettings(optionSettings);
		awsc.createEnvironment(createEnvironmentRequest);

		DescribeEnvironmentsRequest describeEnvironmentsRequest = new DescribeEnvironmentsRequest();
		describeEnvironmentsRequest.withEnvironmentNames(envName);
		DescribeEnvironmentsResult environmentsResult = awsc.describeEnvironments(describeEnvironmentsRequest);
		EnvironmentDescription environmentDescription = environmentsResult.getEnvironments().get(0);
		DescribeLoadBalancersResult lbr = aebc.describeLoadBalancers();
		System.out.println(environmentDescription);
		System.out.println("environment id is " + environmentDescription.getEnvironmentId() + "; environment name is "
				+ environmentDescription.getEnvironmentName());
	}
}