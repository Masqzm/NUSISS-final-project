import { NgModule } from "@angular/core";

// Add modules as needed here
import {
    TuiIcon, TuiIconPipe, TuiLink,
    TuiTitle, TuiButton, TuiAppearance,    
    TuiTextfield, TuiError, TuiSurface
} from '@taiga-ui/core';

import {
    TuiHeader, TuiSearch, TuiCardLarge, 
    TuiCell
} from '@taiga-ui/layout';

import {
    TuiFieldErrorPipe, tuiValidationErrorsProvider, TuiAvatar, 
    TuiChip
} from '@taiga-ui/kit';

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
        TuiFieldErrorPipe,
        TuiSurface, 
        TuiCardLarge,
        TuiCell,
        TuiAvatar, 
        TuiChip
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
        TuiFieldErrorPipe,
        TuiSurface, 
        TuiCardLarge,
        TuiCell,
        TuiAvatar, 
        TuiChip
    ],
    providers: [
        // Ref: https://taiga-ui.dev/pipes/field-error#custom-messages
        tuiValidationErrorsProvider({
            required: 'Enter this!'
        })
    ]
})
export class TaigaUIModule { }