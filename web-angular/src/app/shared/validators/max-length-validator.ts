import { ValidatorFn, AbstractControl, ValidationErrors } from "@angular/forms";

export function MaxLenghtValidator(maxLenght: number): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
        if (control && control.value) {
            return (control.value as string).length > maxLenght ? { maxLenght: true } : null;
        }
        return null;
    }
}