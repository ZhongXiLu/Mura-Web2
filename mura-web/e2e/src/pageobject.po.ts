import { browser, by, element, ElementFinder } from 'protractor';

export class Page {

  public navigateTo(url: string): Promise<unknown> {
    return browser.get(url) as Promise<unknown>;
  }

  public getTitleText(): Promise<string> {
    return element(by.css('h1')).getText() as Promise<string>;
  }

}

