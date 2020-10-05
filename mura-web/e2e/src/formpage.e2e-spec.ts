import { FormPage } from './formpage.po';
import { HomePage } from './homepage.po';
import { browser, logging, by } from 'protractor';

describe('formpage', () => {
  let formpage: FormPage;
  let homepage: HomePage;

  beforeEach(() => {
    formpage = new FormPage();
    homepage = new HomePage();
  });

  it('should start an analysis when form is correctly submitted', () => {
    browser.waitForAngularEnabled(false);   // script timeout workaround

    homepage.navigateTo();
    let prevAnalysesCount = 0;
    homepage.getAllAnalyses().count().then((count) => {
      prevAnalysesCount = count;
    });

    formpage.navigateTo();
    formpage.getGitRepoInput().sendKeys("https://github.com/slub/urnlib");
    expect(formpage.getGitRepoInput().getAttribute('value')).toEqual("https://github.com/slub/urnlib");

    formpage.getAnalysisForm().submit().then(() => {
      browser.getCurrentUrl().then((url) => {
        expect(url).not.toContain("/submit");   // redirect to homepage (i.e. not anymore on form page)

        let analyses = homepage.getAllAnalyses();
        analyses.count().then((count) => {
          expect(count).toEqual(prevAnalysesCount + 1);
          if (count > 0) {
            analyses.get(0).element(by.className("card-header")).getText().then((title) => {
              expect(title).toContain("slub/urnlib");
            });
          }
        });
      })
    });

  });

  afterEach(async () => {
    // Assert that there are no errors emitted from the browser
    const logs = await browser.manage().logs().get(logging.Type.BROWSER);
    expect(logs).not.toContain(jasmine.objectContaining({
      level: logging.Level.SEVERE,
    } as logging.Entry));
  });
});
