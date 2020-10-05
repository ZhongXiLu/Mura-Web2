import { browser, by, element, ElementFinder } from 'protractor';
import { Page } from './pageobject.po';

export class FormPage extends Page {

  public navigateTo(): Promise<unknown> {
    return super.navigateTo(browser.baseUrl + "submit");
  }

  public getAnalysisForm(): ElementFinder {
    return element(by.css('form'));
  }

  public getGitRepoInput(): ElementFinder {
    return element(by.id('gitRepo'));
  }

}
