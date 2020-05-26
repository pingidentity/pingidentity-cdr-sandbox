import { SapphirePage } from './app.po';

describe('SapphirePage', () => {
    let page: SapphirePage;

    beforeEach(() => {
        page = new SapphirePage();
    });

    it('should display welcome message', () => {
        page.navigateTo();
        expect(page.getTitleText()).toEqual('Welcome to Sapphire!');
    });

});
