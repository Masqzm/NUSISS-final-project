import { NgModule } from "@angular/core";

// Add modules as needed here
import {
    TuiIcon, TuiIconPipe, TuiLink,
    TuiTitle, TuiButton, TuiAppearance,    
    TuiTextfield, TuiError, TuiSurface
} from '@taiga-ui/core';

import {
    TuiHeader, TuiSearch, TuiCardLarge, 
    TuiCell, TuiForm
} from '@taiga-ui/layout';

import {
    TuiFieldErrorPipe, tuiValidationErrorsProvider, TuiAvatar, 
    TuiChip, TuiPassword
} from '@taiga-ui/kit';
import { of } from "rxjs";

@NgModule({
    imports: [
        // Add modules as needed here
        TuiIcon, TuiIconPipe, TuiLink,
        TuiTitle, TuiHeader, TuiButton,
        TuiAppearance, ...TuiSearch, ...TuiTextfield,
        TuiError, TuiFieldErrorPipe, TuiSurface, 
        TuiCardLarge, TuiCell, TuiAvatar, 
        TuiChip, TuiForm, TuiPassword
    ],
    exports: [
        // Add modules as needed here
        TuiIcon, TuiIconPipe, TuiLink,
        TuiTitle, TuiHeader, TuiButton,
        TuiAppearance, ...TuiSearch, ...TuiTextfield,
        TuiError, TuiFieldErrorPipe, TuiSurface, 
        TuiCardLarge, TuiCell, TuiAvatar, 
        TuiChip, TuiForm, TuiPassword
    ],
    providers: [
        // Ref: https://taiga-ui.dev/pipes/field-error#custom-messages
        tuiValidationErrorsProvider({
            required: 'Enter this!',
            email: 'Please enter a valid email',
            minlength: ({requiredLength}: {requiredLength: string}) =>
                of(`Minimum characters — ${requiredLength}`),
            maxlength: ({requiredLength}: {requiredLength: string}) =>
                `Maximum characters — ${requiredLength}`,
            pattern: 'Only alphanumeric characters and underscores allowed'
        })
    ]
})
export class TaigaUIModule { }