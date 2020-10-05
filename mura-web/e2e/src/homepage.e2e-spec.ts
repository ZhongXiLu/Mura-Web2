import { HomePage } from './homepage.po';
import { browser, logging } from 'protractor';

describe('homepage', () => {
  let homepage: HomePage;

  beforeEach(() => {
    homepage = new HomePage();
  });

  it('should display correct title', () => {
    homepage.navigateTo();
    expect(homepage.getTitleText()).toEqual('MuRa Web Interface');
  });

  it('should route to the correct page when the analysis button is pressed', () => {
    homepage.navigateTo();
    homepage.getStartAnalysisButton().click().then(() => {
      browser.getCurrentUrl().then((url) => {
        expect(url).toContain("/submit");
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
