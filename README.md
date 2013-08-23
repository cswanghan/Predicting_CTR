Predicting_CTR
=============

Developer	: Zicong Zhou; Deepak Ananth Rama; Xu Zhang; Jason Lu
email		: deepakar@icloud.com, jaesanx@cs.ucla.edu, 
Date		: March 2013
For		: Research paper titled "Predicting Click-Through Rate for New Users and Advertisers"

IDE		: Eclipse
Language	: Java

Project to Predict Click Through Rate in online advertising for new users and advertisers.

Details of our training data set can be found here http://www.kddcup2012.org/c/kddcup2012-track2

An example run for replicating results:

1) add all the txt files from run-files folder into the same folder of src-files (referred as project)
2) download lib linear http://www.csie.ntu.edu.tw/~cjlin/liblinear/ and run "make" on the project
3) run LRFeaturePredictor.java, it should output 1.txt and 1_test.txt in the local path of the project.
4) copy 1.txt, 1_test.txt, scoreKDD.py and newSol.txt (these 2 are from project.zip) to the project folder of lib linear
5) in terminal navigate to liblinear folder
6) run ./train -s 0 1.txt 1.model (this should generate 1.model)
7) run ./predict -b 1 1_test.txt 1.out (this should generate 1.out)
8) run awk '{for(i=3;i<=NF;++i)print $i}' temp.out > result.txt (gets the 3rd column needed to compare with newSol.txt)
9) run tail -n 268554 results.txt > final.txt (gets rid of unnecessary line spacing)
10) run python scoreKDD.py newSol.txt final.txt (this should generate the AUC, NWMAE, WRMSE metrics)

This package contains the following files:

All the classes with (main) by them are run independently to generate the result files. Main two classes to run are the LRFeaturePredictor.java and TokenFeaturePredictor.java

-Baseline.java

An initial implementation of token level CTR prediction, now replaced by TokenFeaturePredictor.java

-DataSetGenerator.java (main)
A preprocessor that generates newTest.txt, newUserTest.txt, and newAdvTest.txt which represent new users and advertisers, new users, and new advertisers respectively. These are the testing data sets used in our program.

-DataStats.java (main)
Does some data analytics on the training.txt file.

-LRFeaturePredictor.java (main)
Linear Regression model for CTR prediction. Can set to various features within this through hardcoding.

-TableLoader.java
Maps feature to values used in LRFeaturePredictor.

-TestDataExtractor.java
Another data analytics extractor mainly for users.

-TokenFeaturePredictor.java (main)
Token level feature predictor

-FirstModel.java (main)
Used in feature engineering for term ctr

Sidenote: We originally had 5 members in our group but after maybe the 7th or 8th week one of our group mates dropped the course.
