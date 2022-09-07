import { AngularEditorConfig } from "@kolkov/angular-editor"

export function getNoticiaEditorConfig(): AngularEditorConfig {
    return {
        editable: true,
        spellcheck: false,
        height: 'auto',
        minHeight: '0',
        maxHeight: 'auto',
        width: 'auto',
        minWidth: '0',
        translate: 'yes',
        enableToolbar: true,
        showToolbar: true,
        placeholder: 'Digite seu texto para gerar um HTML...',
        defaultParagraphSeparator: '',
        defaultFontName: '',
        defaultFontSize: '4',
        fonts: [{ class: 'arial', name: 'Arial' },],
        toolbarHiddenButtons: [[], ['insertVideo']]
        // toolbarHiddenButtons: [[], ['insertImage', 'insertVideo']]
    }
};