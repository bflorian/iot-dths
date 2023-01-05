/**
 *  Directional Motion Sensor
 *
 *  Copyright 2016 Bob Florian
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */
metadata {
	definition (name: "Directional Motion Sensor", namespace: "bflorian", author: "Bob Florian") {
		capability "Motion Sensor"
		capability "Sensor"
        capability "Refresh"

        tiles(scale: 2) {
            multiAttributeTile(name:"motion", type: "generic", width: 6, height: 4){
                tileAttribute ("device.motion", key: "PRIMARY_CONTROL") {
                    attributeState "active", label:'motion', icon:"st.motion.motion.active", backgroundColor:"#53a7c0"
                    attributeState "inactive", label:'no motion', icon:"st.motion.motion.inactive", backgroundColor:"#ffffff"
                }
            }

            standardTile("refresh", "device.switch", inactiveLabel: false, height: 2, width: 2, decoration: "flat") {
                state "default", label:"", action:"refresh.refresh", icon:"st.secondary.refresh"
            }        

            main(["motion"])
            details(["motion", "refresh"])
        } 
    }
}

def refresh() {
	log.trace("refresh()")
	parent.refresh()
}