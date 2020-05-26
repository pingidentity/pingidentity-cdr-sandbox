import { Pipe, PipeTransform } from '@angular/core';
import { propPath } from '@app/core/helpers';

@Pipe({
  name: 'propPath'
})
export class PropPathPipe implements PipeTransform {

    transform(value: any, ...args: any[]): any {
        return propPath(value, args[0]);
    }

}
