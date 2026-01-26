JavaCard Practice
========

This repository provides tools and libraries for JavaCard development, including APDU handling utilities.

Library Overview
========

The core logic is separated into libraries located in the `library` directory.

* **APDU for Java**
    * Utilities for encoding the command of APDU and decoding the response representation of binary data.
    * [library/apdu](library/apdu)
* **APDU Command helper for Java**
    * Helper classes for building APDU commands.
    * [library/apdu-command](library/apdu-command)

Modules
========

* [library](library): Contains the core APDU libraries.
* [android-app](android-app): Android application demonstrating the usage of the libraries.
* [javacard-applet](javacard-applet): Sample JavaCard applet.

License
========

```
Copyright 2022-2025 ARIYAMA Keiji

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
