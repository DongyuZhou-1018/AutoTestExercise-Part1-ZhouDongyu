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

//1. Open target website
WebUI.openBrowser('')

WebUI.navigateToUrl('https://www.baidu.com/')

WebUI.takeScreenshot()

WebUI.click(findTestObject('Object Repository/Page_/span__soutu-btn'))

WebUI.takeScreenshot()

String picPath = RunConfiguration.getProjectDir() + '\\Test Cases\\testData\\cartoon.jpg'

WebUI.uploadFile(findTestObject('Object Repository/Page_/input__upload-pic'), picPath)

WebUI.takeScreenshot()

WebUI.waitForPageLoad(10)

//2. get the sessionid of the original search result page
String url = WebUI.getUrl()

WS.comment(url)

String sessionId_ori = CustomKeywords.'autotest.comm.getSessionIDFromURL'(url)

WS.comment(sessionId_ori)

//3. get specified VISITRESULT from config file
File newFile = new File('Test Cases/testData/config.txt')

String searchResultTmp = newFile.text

WS.comment(searchResultTmp)

int searchResultItem = 0

if (searchResultTmp != '') {
    String[] searchResult = searchResultTmp.split('=')

    searchResultItem = Integer.parseInt((searchResult[1]).replace('"', ''))

    WS.comment(searchResultItem.toString())

    //4. Click the sepcified item
//    String urlOfSepcifiedItem = WebUI.getAttribute(findTestObject('Page_/span__general-imgcol-item-bg graph-imgbg-fff', 
//            [('data-index') : searchResultItem]), 'href')
//
//    WS.comment(urlOfSepcifiedItem)

    WebUI.click(findTestObject('Page_/span__general-imgcol-item-bg graph-imgbg-fff', [('data-index') : searchResultItem]))

    WebUI.takeScreenshot()

    //5. assert sessionid is same as the original search result page to assert the search results are related to the used images
    String searchItemPageURL = WebUI.getUrl()

    WS.comment(searchItemPageURL)

    String sessionId_after = CustomKeywords.'autotest.comm.getSessionIDFromURL'(searchItemPageURL)

    WS.comment(sessionId_after)

    assert sessionId_after == sessionId_ori
} else {
    WS.comment('No index specified!')
}

