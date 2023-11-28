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

WebUI.openBrowser()
WebUI.navigateToUrl('https://www.baidu.com/')

WebUI.takeScreenshot()

WebUI.clickImage(findTestObject('span.soutu-btn'))

WebUI.takeScreenshot()

WebUI.clickImage(findTestObject('input.upload-pic'))

WebUI.takeScreenshot()

WebUI.setText(findTestObject(null), 'C:\\Test Cases\\testData\\test.jpg')

WebUI.takeScreenshot()

WebUI.sendKeys(findTestObject(null), 'ENTER')

WebUI.takeScreenshot()

WebUI.waitForPageLoad(10)

WebUI.takeScreenshot()

String url = WebUI.getUrl()

String[] str = searchResultTmp.split('&')

String[] str2 = []

String sessionId
//get the sessionid of the original search result page
for (int i = 0; i < str.length(); i++) {
    str2 = (str[i]).split('=')

    if ((str2[0]) == 'session_id') {
        sessionId = (str[1])
    }
    
    break
}

//get specified VISITRESULT
File newFile = new File('testData/config.txt')

String searchResultTmp = newFile.text

int searchResultItem = 0

if (searchResultTmp != '') {
    String[] searchResult = searchResultTmp.split('=')

    searchResultItem = Integer.parseInt((searchResult[1]))
	//open the specified result item
    WebUI.navigateToUrl('https://graph.baidu.com/pcpage/similar?carousel=503&entrance=GENERAL&extUiData%5BisLogoShow%5D=1&image=http%3A%2F%2Fmms0.baidu.com%2Fit%2Fu%3D13389876,3136556651%26fm%3D253%26app%3D138%26f%3DJPEG%3Fw%3D250%26h%3D250&index=' + 
		searchResultItem.toString() + '&inspire=general_pc&next=2&originSign=126be33fdbccc632dcc4401701164678&page=1&render_type=carousel&searchItemPageURL14977701778980248844&shituToken=5540b9&sign=126be33fdbccc632dcc4401701164678&srcp=crs_pc_similar&tpl_from=pc')
	WebUI.takeScreenshot()
}

String searchItemPageURL = WebUI.getUrl()
//assert sessionid is same as the original search result page
WS.verifyMatch(searchItemPageURL, '.*&session_id=' + sessionId + '.*', true)

