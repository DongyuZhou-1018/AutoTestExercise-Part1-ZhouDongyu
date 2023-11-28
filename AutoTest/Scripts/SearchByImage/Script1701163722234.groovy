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

WebUI.openBrowser('')

WebUI.navigateToUrl('https://www.baidu.com/')

WebUI.takeScreenshot()

WebUI.click(findTestObject('Object Repository/Page_/span__soutu-btn'))

WebUI.takeScreenshot()

String picPath = RunConfiguration.getProjectDir() + '\\Test Cases\\testData\\cartoon.jpg'

WebUI.uploadFile(findTestObject('Object Repository/Page_/input__upload-pic'), picPath)

WebUI.takeScreenshot()

WebUI.waitForPageLoad(10)

//get the sessionid of the original search result page
String url = WebUI.getUrl()

WS.comment(url)

String[] str = url.split('&')

String[] str2 = []

String sessionId = ''

for (int i = 0; i < str.length; i++) {
    WS.comment(str[i])

    str2 = (str[i]).split('=')

    if ((str2[0]) == 'session_id') {
        sessionId = (str2[1])

        WS.comment(sessionId)

        break
    }
}

WS.comment(sessionId)

//get specified VISITRESULT
File newFile = new File('Test Cases/testData/config.txt')

String searchResultTmp = newFile.text

WS.comment(searchResultTmp)

int searchResultItem = 0

if (searchResultTmp != '') {
    String[] searchResult = searchResultTmp.split('=')

    searchResultItem = Integer.parseInt((searchResult[1]).replace('"', ''))

    WS.comment(searchResultItem.toString())

    //Click the sepcified item
    WebUI.click(findTestObject('Object Repository/Page_/span__general-imgcol-item-bg graph-imgbg-fff', [('index') : searchResultItem]))

    WebUI.takeScreenshot()

    //assert sessionid is same as the original search result page
    String searchItemPageURL = WebUI.getUrl()

    WS.verifyMatch(searchItemPageURL, ('.*&session_id=' + sessionId) + '.*', true)
} else {
    WS.comment('No index specified!')
}

