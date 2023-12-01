I used Katalon Studio to implement the Auto Test of Part1, since there is no access to Google, I used Baidu instead :)
- The test script is under AutoTest\Scripts\SearchByImage\
- Test Keyword/library is under AutoTest\Keyword\autotest
    - comm.groovy: the extract session id keyword
    - image.groovy: java class for compare 2 images (reuse code from internet)
- Test data is under AutoTest\Test Cases\testData including: input image, config file
- Test Object of WebUI is under Object Repository: the web ui elements identified with xpath.

Katalon Studio can be found: https://katalon.com/, it requires to register an account to activate.
