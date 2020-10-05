import { FormPage } from './formpage.po';
import { HomePage } from './homepage.po';
import { browser, logging, by } from 'protractor';

describe('formpage', () => {
  let formpage: FormPage;
  let homepage: HomePage;

  let originalTimeout: number;

  beforeEach(() => {
    formpage = new FormPage();
    homepage = new HomePage();
    originalTimeout = jasmine.DEFAULT_TIMEOUT_INTERVAL;
    jasmine.DEFAULT_TIMEOUT_INTERVAL = 1000000;
  });

  it('should start an analysis when form is correctly submitted', () => {
    browser.ignoreSynchronization = true;  // avoid infinitely waiting/loading

    homepage.navigateTo();
    let prevAnalysesCount = 0;
    homepage.getAllAnalyses().count().then((count) => {
      prevAnalysesCount = count;
    });

    formpage.navigateTo();
    formpage.getGitRepoInput().sendKeys("https://github.com/slub/urnlib");
    expect(formpage.getGitRepoInput().getAttribute('value')).toEqual("https://github.com/slub/urnlib");

    formpage.getAnalysisForm().submit().then(() => {
      // wait until new page is loaded (i.e. redirect to homepage)
      browser.wait(browser.ExpectedConditions.not(browser.ExpectedConditions.urlContains("submit")), 10000).then(() => {  
        browser.getCurrentUrl().then((url) => {

          expect(url).not.toContain("/submit");   // homepage (i.e. not anymore on form page)

          // some generous time to wait until backend to start the analysis
          browser.sleep(60000);

          homepage.getAllAnalyses().then((analyses) => {
            expect(analyses.length).toEqual(prevAnalysesCount + 1);
            if (analyses.length > 0) {
              // 0 is the most recent one
              analyses[0].element(by.className("card-header")).getText().then((title) => {
                expect(title).toContain("slub/urnlib");
              });
            }
          });
        });
      })
    });

  });

  afterEach(async () => {
    jasmine.DEFAULT_TIMEOUT_INTERVAL = originalTimeout;
    
    // Assert that there are no errors emitted from the browser
    const logs = await browser.manage().logs().get(logging.Type.BROWSER);
    expect(logs).not.toContain(jasmine.objectContaining({
      level: logging.Level.SEVERE,
    } as logging.Entry));
  });
});
