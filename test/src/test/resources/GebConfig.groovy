import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions


atCheckWaiting = true

waiting
		{
			timeout = 30
			retryInterval = 0.5
		}

environments {
	chrome {
		driver = {
			ChromeOptions o = new ChromeOptions()
			o.addArguments("window-size=1920,1080")
			new ChromeDriver(o)
		}
	}
	chromeHeadless {
		driver = {
			ChromeOptions o = new ChromeOptions()
			o.addArguments("headless", "no-sandbox", "window-size=1920,1080")
			new ChromeDriver(o)

		}
	}
}

reportOnTestFailureOnly = true

baseUrl = "${System.env.BAMBOO_URL}"
