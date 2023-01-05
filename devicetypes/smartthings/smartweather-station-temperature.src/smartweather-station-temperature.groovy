/**
 *  SmartWeather Station Temperature
 *
 *  Copyright 2019 Discovery Home
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WASmartRRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 */
metadata {
	definition (name: "SmartWeather Station Temperature", namespace: "smartthings", author: "Discovery Home", cstHandler: true) {
		capability "Temperature Measurement"
	}


	simulator {
		// TODO: define status and reply messages here
	}

    tiles(scale: 2) {
        valueTile("temperature", "device.temperature", height: 2, width: 2) {
            state "default", label:'${currentValue}°',
                    backgroundColors:[
                            [value: 31, color: "#153591"],
                            [value: 44, color: "#1e9cbb"],
                            [value: 59, color: "#90d2a7"],
                            [value: 74, color: "#44b621"],
                            [value: 84, color: "#f1d801"],
                            [value: 95, color: "#d04e00"],
                            [value: 96, color: "#bc2323"]
                    ]
        }
	}
}

// parse events into attributes
def parse(String description) {

}