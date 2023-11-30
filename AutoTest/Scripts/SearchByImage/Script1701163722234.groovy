import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testng.keyword.TestNGBuiltinKeywords as TestNGKW
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import internal.GlobalVariable as GlobalVariable
import org.openqa.selenium.Keys as Keys
import com.kms.katalon.core.configuration.RunConfiguration as RunConfiguration
import org.openqa.selenium.Rectangle as Rectangle
import javax.imageio.ImageIO
import java.awt.image.BufferedImage


//1. Open target website
WebUI.openBrowser('')
WebUI.maximizeWindow()
WebUI.navigateToUrl('https://www.baidu.com/')
WebUI.click(findTestObject('Object Repository/Page_baidu/span__soutu-btn'))
String picPath = RunConfiguration.getProjectDir() + '\\Test Cases\\testData\\cartoon.png'
WebUI.uploadFile(findTestObject('Object Repository/Page_baidu/input__upload-pic'), picPath)
WebUI.waitForPageLoad(30)

//2. get the sessionid of the original search result page
String url_ori = WebUI.getUrl()
String sessionId_ori = CustomKeywords.'autotest.comm.getSessionIDFromURL'(url_ori)

//3. get specified VISITRESULT from config file
File newFile = new File('Test Cases/testData/config.txt')
String searchResultTmp = newFile.text
String[] searchResult = searchResultTmp.split('=')
searchResultItem = Integer.parseInt((searchResult[1]).replace('"', ''))
WS.comment(searchResultItem.toString())

//4. Click the sepcified picture item, and save the snapshot to Test Cases/testData/lastPage.png
WebUI.click(findTestObject('Page_baidu/span__general-imgcol-item-bg graph-imgbg-fff', [('data-index') : searchResultItem]))
WebUI.takeScreenshot('Test Cases/testData/lastPage.png')

//    //5. assert sessionid is same as the original search result page to assert the search results are related to the used images
//    String searchItemPageURL = WebUI.getUrl()
//
//    String sessionId_after = CustomKeywords.'autotest.comm.getSessionIDFromURL'(searchItemPageURL)
//
//    assert sessionId_after == sessionId_ori 
//
	
//6. compare search result is related with the original input image.
//get the snapshot of the image of search result, and compare with the original input photo
String saveImgFileName = RunConfiguration.getProjectDir() + '\\Test Cases\\testData\\searchResult.png'
WebUI.takeElementScreenshot(saveImgFileName, findTestObject('Page_baidu/span__general-imgcol-item-bg graph-imgbg-fff', [('data-index') : searchResultItem]),FailureHandling.CONTINUE_ON_FAILURE)

//resize original image
String oriImgFileName = RunConfiguration.getProjectDir() + '\\Test Cases\\testData\\cartoon.png'
int width = 200
int height = 300
File file = new File(oriImgFileName)
BufferedImage img = ImageIO.read(file)
BufferedImage image  = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR)
image.getGraphics().drawImage(img,0,0,width,height,null)

String destFileName = RunConfiguration.getProjectDir() + '\\Test Cases\\testData\\cartoon_resize.png'
File destFile = new File(destFileName)
ImageIO.write(image,"png",destFile)
//resize to same size of snapshot of first image of search result 
saveImgFileName = RunConfiguration.getProjectDir() + '\\Test Cases\\testData\\searchResult.png'
File file2 = new File(saveImgFileName)
BufferedImage img2 = ImageIO.read(file2)
BufferedImage image2  = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR)
image2.getGraphics().drawImage(img2,0,0,width,height,null)


String destFileName2 = RunConfiguration.getProjectDir() + '\\Test Cases\\testData\\searchResult_resize.png'
File destFile2 = new File(destFileName2)
ImageIO.write(image2,"png",destFile2)
//compare 2 images(found an implementation from Internet), the source file is under Keywords\\autotest\\images.groovy
InputStream InputStream1 = new FileInputStream(destFileName)
InputStream InputStream2 = new FileInputStream(destFileName2)
imageComparisonResult = CustomKeywords.'autotest.images.imageComparison'(InputStream1,InputStream2)
assert(imageComparisonResult * 100 >= 10)

WebUI.closeBrowser()