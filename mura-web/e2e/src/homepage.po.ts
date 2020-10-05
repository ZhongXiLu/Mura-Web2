import { browser, by, element, ElementFinder, ElementArrayFinder } from 'protractor';
import { Page } from './pageobject.po';

export class HomePage extends Page {

  public navigateTo(): Promise<unknown> {
    return super.navigateTo(browser.baseUrl);
  }

  public getStartAnalysisButton(): ElementFinder {
    return element(by.linkText('Start an Analysis'));
  }

  public getAllAnalyses(): ElementArrayFinder {
    return element.all(by.className('card'));
  }

}
