export class CoverageModel {
    method1(num: Number): void {
        if (num > 4) {
            console.log(num);
        }

        if (num > 8) {
            console.log(num);
        }

        if (num === 9) {
            return
        }

        if (num > 19) {
            console.log(num);
        }

        if (num > 24) {
            console.log(num);
        }
    }

    method2(num: Number): void {
        if (num > 4) {
            console.log(num);
        }

        if (num > 8) {
            console.log(num);
        }
    }
}