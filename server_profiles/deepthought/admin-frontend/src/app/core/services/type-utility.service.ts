import {Injectable} from '@angular/core';
import {CurrencyPipe, PercentPipe} from '@angular/common';
import { _ } from 'underscore';
import * as moment from 'moment';


@Injectable({
    providedIn: 'root'
})
export class TypeUtilityService {

    constructor(
        private percentPipe: PercentPipe,
        private currencyPipe: CurrencyPipe
    ) {}

    convertDuration(period: string) {
        if (period === null || period === '') {
            return 'N/A';
        }

        if (period.match(/^P(\d+)D$/)) {
            const days = period.match(/^P(\d+)D$/)[1];
            return days === '1' ? '1 Day' : days + ' days';
        } else if (period.match(/^P(\d+)M$/)) {
            const months = period.match(/^P(\d+)M$/)[1];
            return months === '1' ? '1 Month' : months + ' months';
        } else if (period.match(/^P(\d+)Y$/)) {
            const years = period.match(/^P(\d+)Y$/)[1];
            return years === '1' ? '1 Year' : years + ' years';
        }

        return moment.duration(period).humanize(false);
    }

    convertRateString(rateString: any) {
        return this.percentPipe.transform(rateString, '1.2-4');
    }

    getUniqueKeyList(keyName: string, objectList) {
        return _.chain(objectList).map(item => item[keyName]).uniq().value();
    }

    convertValueString(valueString: string) {
        return this.currencyPipe.transform(valueString);
    }
}
