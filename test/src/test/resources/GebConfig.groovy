import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions

waiting {
	timeout = 2
}

environments {	
	// run via “./gradlew chromeTest”
	chrome {
		driver = { 
            new ChromeDriver() 
        }
	}

	// run via “./gradlew chromeHeadlessTest”
	chromeHeadless {
		driver = {
			ChromeOptions o = new ChromeOptions()
			o.addArguments("headless", "no-sandbox", "window-size=1920,1080")
			new ChromeDriver(o)
		}
	}
}

baseUrl = "${System.env.BAMBOO_URL}"
