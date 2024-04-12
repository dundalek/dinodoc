import { expect, test } from "bun:test";
import { greet } from "../src/greeter";
import "../readme"

test("greeter", () => {
  expect(greet("John")).toBe("Hello, John");
});
