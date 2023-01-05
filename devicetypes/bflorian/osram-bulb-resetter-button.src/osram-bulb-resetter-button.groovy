/**
 *  Osram Bulb Resetter Button
 *
 *  Copyright 2021 Bob Florian
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 */
metadata {
	definition (name: "Osram Bulb Resetter Button", namespace: "bflorian", author: "Bob Florian") {
		capability "Momentary"
	}

	tiles {
		// TODO: define your main and details tiles here
	}
}

def parse(String description) {
}

// handle commands
def push() {
	log.debug "Executing 'push'"
	parent.start()
}