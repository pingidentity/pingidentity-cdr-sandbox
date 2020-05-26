import { Injectable } from '@angular/core';
import { FormFieldType, ResponseGetTypes, TypeService } from '@bizaoss/deepthought-admin-angular-client';

@Injectable({
    providedIn: 'root'
})
export class TypeManagementService {

    private typeMap: ResponseGetTypes = {};
    private loading = false;

    constructor(private typeService: TypeService) {}

    public populateTypes(typeNames: FormFieldType[] & string[], overwrite: boolean = false) {
        // Strip out types we have already retrieved
        if (!overwrite) {
            typeNames = typeNames.filter((typeName) => !this.typeMap[typeName]);
        }

        if (typeNames.length > 0) {
            this.loading = true;

            this.typeService.getTypes(typeNames).subscribe(returnedTypes => {
                for (const oneType of typeNames) {
                    this.typeMap[oneType] = returnedTypes[oneType];
                }
                this.loading = false;
            });
        }
    }

    public isLoading() {
        return this.loading;
    }

    public getTypeByValue(typeName, typeValue) {
        if (!this.typeMap[typeName]) {
            throw new Error(`Type "${typeName}" not loaded :(`);
        }

        return this.typeMap[typeName].find(valueKey => valueKey.value === typeValue);
    }

    public getLabel(typeName, typeValue) {
        const typeResult = this.getTypeByValue(typeName, typeValue);
        return typeResult ? typeResult.label : null;
    }

    public getStyle(typeName, typeValue) {
        const typeResult = this.getTypeByValue(typeName, typeValue);
        return typeResult ? typeResult.style : null;
    }

    public getIcon(typeName, typeValue) {
        const typeResult = this.getTypeByValue(typeName, typeValue);
        return typeResult ? typeResult.icon : null;
    }
}
