import { NgModule } from "@angular/core";

// Add modules as needed here
import {
    TuiIcon, 
    TuiIconPipe, 
    TuiLink,
    TuiTitle,
    TuiButton,
    TuiAppearance,
    TuiTextfield,
    TuiError
} from '@taiga-ui/core';

import {TuiHeader, TuiSearch} from '@taiga-ui/layout';

import {TuiFieldErrorPipe, tuiValidationErrorsProvider} from '@taiga-ui/kit';

import {tuiMarkControlAsTouchedAndValidate} from '@taiga-ui/cdk';

@NgModule({
    imports: [
        // Add modules as needed here
        TuiIcon,
        TuiIconPipe,
        TuiLink,
        TuiTitle,
        TuiHeader,
        TuiButton,
        TuiAppearance,
        ...TuiSearch,
        ...TuiTextfield,
        TuiError,
        TuiFieldErrorPipe
    ],
    exports: [
        // Add modules as needed here
        TuiIcon,
        TuiIconPipe,
        TuiLink,
        TuiTitle,
        TuiHeader,
        TuiButton,
        TuiAppearance,
        ...TuiSearch,
        ...TuiTextfield,
        TuiError,
        TuiFieldErrorPipe
    ],
    providers: [
        // Ref: https://taiga-ui.dev/pipes/field-error#custom-messages
        tuiValidationErrorsProvider({
            required: 'Enter this!'
        })
    ]
})
export class TaigaUIModule { }