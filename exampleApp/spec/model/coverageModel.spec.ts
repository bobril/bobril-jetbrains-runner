import { CoverageModel } from "../../src/model/coverageModel";

describe("CoverageModel", () => {
    let model: CoverageModel;

    beforeEach(() => {
        model = new CoverageModel();
    });

    it("method1", () => {
        model.method1(4)
    })

    it("method1", () => {
            model.method1(5)
        })
});