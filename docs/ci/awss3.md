# Amazon S3
The CoffeeNowApp CI process stores build and test artifacts in an Amazon S3
Bucket. When Travis CI completes a build the artifacts will be stored in a
publicly accessible S3 bucket where developers can access and review build
output.

# Setup an S3 Bucket
Described below are the basic steps needed to setup an AWS S3 bucket. Amazon
has a lot of good documentation on the topic which can be found at:
[http://docs.aws.amazon.com/AmazonS3/latest/UG/Welcome.html](http://docs.aws.amazon.com/AmazonS3/latest/UG/Welcome.html)

Here are the basics:
1. Create or sign into your AWS account at
[http://aws.amazon.com/](http://aws.amazon.com/).
2. Goto the [S3 service console](https://console.aws.amazon.com/s3) and create
a bucket. Name the bucket and select a region. The CoffeeNowApp uses the Oregon
(us-west-2) region.
3. Select the newly created bucket and choose the Properties tab from the top
right of the screen. Expand the permissions section and click the "Edit bucket
policy" button. A pop-up dialog should appear. Paste in the following json and
replace YOUR_BUCKET_NAME with the name of the bucket you created:
```json
{
	"Version": "2012-10-17",
	"Statement": [
		{
			"Sid": "AddPerm",
			"Effect": "Allow",
			"Principal": "*",
			"Action": "s3:GetObject",
			"Resource": "arn:aws:s3:::YOUR_BUCKET_NAME/*"
		}
	]
}
```
This bucket policy makes content uploaded to the bucket publicly accessible.
For a project publicly hosted on github, the results of the build are likely
ok to share with the public too but consider a using a different policy if your
project is more sensitive. You can read more about Amazon S3 bucket policies
[here](https://docs.aws.amazon.com/AmazonS3/latest/dev/example-bucket-policies.html).
4. While you are still on the permissions section, click the "Add more
permissions" button. For Grantee select the "Any Authenticated AWS User" option
and provide them with the "List" and "Upload/Delete" permissions. Save these
settings. Now we need to create a user that will be used upload artifacts from
Travic CI to Amazon S3.
5. To create an AWS user go to the
[IAM service console](https://console.aws.amazon.com/iam), click on "Users"
option on the right side of the screen and click "Create New Users". Provide
a name that is easy to remember, maybe with the project name included so it is
easy to associate the user with the bucket name. Be sure the "Generate an
access key for each user" option is selected. The user should be created and
clicking the "Show User Security Credentials" will show you the Access Key ID
and Secret Access Key. You'll need these later. Note: these keys are very
sensitive. Do not upload them to a git repository in plain text or share them
with people you don't trust. A malicious user with these keys can upload
content to your S3 account and rack up a massive bill. In the Travis YAML
section I'll go over how to securely include these keys into your project.

With this, your S3 bucket is now setup to be publicly viewable but only signed
in users can upload content. We will provide Travis CI with the credentials for
the user we just created so the automation can upload results.