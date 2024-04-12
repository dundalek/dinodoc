import { main } from "./src/app"
import * as greeter from "./src/greeter"

// # Hello world
// Main entrypoint prints the greeting
main
  // Prepares the greeting message
  ; greeter.greet

//
// # Alternative nested padding with blocks in curlies
//
// Main entrypoint prints the greeting
main
{
  // Prepares the greeting message
  greeter.greet
}
