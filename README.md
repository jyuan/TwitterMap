# TwitterMap

## Objectives:

- Reads a stream of tweets from the Twitter Live API
- Records the tweet ID, location info, username, time, category, cotent and other relevant elements into a DB (Here, I use Amazon RDB)
- Presents the Tweet in a map that is being updated in Near Real Time (Need Improved, here I just use timer in JS)
- The map clusters tweets as to show where is people tweeting the most, according to the sample tweets we get from the streaming API.
 
## Heatmap
A heatmap is a visualization used to depict the intensity of data at geographical points

Here, I use the [Google Maps Javascript Heatmap API](https://developers.google.com/maps/documentation/javascript/examples/layer-heatmap)

## Internal Design
### Features

- Display twitters' location on map with predefined keywords search ('halloween', 'job', 'music', 'movies' etc.)
- Color gradient and density map display for twitts on map based on location
- Real time twitts location display
- Auto create AWS Elastic beanstalk application, environment, and deploy TwittMap application

### Tools used

- Web server: Tomcat 8.0 on AWS Elastic Beanstalk
- Database: RDB on AWS EC2
- API: Twitter Live and Streaming API, Google Map API,Elastic Beanstalk,Elastic LoadBalancing

### Steps in using source code

- Git clone source code
- Link to AWS account with access key and secret key and connect with current development environment
- Create application and environment on AWS Elastic Beanstalk
- Add jars to build path (I didn't upload)
- Create RDB Database on AWS EC2
- Create account in Twitter API and get access keys
- Deploy project in AWS Elastic Beanstalk
- You should add your own key into [AWS](https://github.com/jyuan/TwitterMap/blob/master/build/classes/AwsCredentials.properties.example) and [config](https://github.com/jyuan/TwitterMap/blob/master/build/classes/config.properties.example) properties, and rename it as `config.properties` and `AwsCredentials.properties`

## Website Demo
[TwitterMap - Heatmap](http://twittermap2-jmmjybygpr.elasticbeanstalk.com/)

## Comment
If you have any questions, you can add `issue` or `pull request` if you can improve it. OR contact me by email.

