# UIPDroid
UIPDroid enforces the users' basic right-to-know through user interfaces whenever an app uses permissions, and provides a more fine-grained UI widget-level permission control that can allow, deny, or produce fake private data dynamically for each permission use in the app at the choice of users, even if the permissions may have been granted to the app at the application level. In addition, to make the tool easier for end users to use, unlike some other root-based solutions, UIPDroid is root-free, developed as a module on top of a virtualization framework that can be installed onto users' device as a usual app. 

## Key Features
<ul>
  <li> Fine-grained Permission Management consists of basic permission managament and widget-level permission management</li>
  <li> An additional choice, Ruturn Fake Data, for permission request</li>
  <li> Permission Usage Report</li>
</ul>

## Installation

<ul>
  <li> Install <a href ="https://github.com/android-hacker/VirtualXposed">VirtualXposed</a></li>
  <li> Grant permissions (e.g. Contacts, Location, Storage, etc) to Virtual Xposed</li>
  <li> Install and Activate this module</li>
  <li> Click Reboot in the menu to restart Virtual Xposed and make module effiective </li>
</ul>

## Usage Example
### Permission Management
Users can manage their permission preferences for different apps or UI widgets. Basic Permission Management lists all permissions requested by an app, which is similar to Android system's native permission manager. Users can switch to Widget-Level Permission Management in which UIPDroid lists permissions triggered by each widget in a format of {PermissionName_WidgetID}. One permission can appear multiple times under this widget-level setting as the same per-mission may be linked to different UI widgets, through which we enable more fine-grained control for user-aware permission uses.</br>

<nobr><img src="https://user-images.githubusercontent.com/79134822/143244187-d8224098-0021-4e74-8e85-aa82b5430cec.png" width="200" height="400" alt="applicationlevel"/>
<img src="https://user-images.githubusercontent.com/79134822/143244212-433c621b-7fd3-44a7-bdc3-ca21320c93ab.png" width="200" height="400" alt="widgetlevel"/></nobr>


### Permission Usage Report
Users can inspect the logs of apps' permission uses visually in two formats. One is Timeline Report that displays all permission uses in a reverse chronological order. The other is Summary Report that lists the total numbers of use times of a permission and the last access time. Users can filter the reports by a specific permission or an application.</br>

<nobr><img src="https://user-images.githubusercontent.com/79134822/143248606-165297d3-3ced-43fe-9eef-254bbf178b29.png" width="200" height="400" alt="summaryreport"/>
<img src="https://user-images.githubusercontent.com/79134822/143248614-f72094db-314b-4c92-88df-391f039ce417.png" width="200" height="400" alt="timelinereport"/></nobr>
## Note

<ul>
  <li> All Android Code can be found in app folder</li>
  <li> A predefined permission preferences file can be found in config folder. User can easily import this file to reduce the cognitive and decision burdens</li>
</ul>

## Credits
<li><a href ="https://smc.smu.edu.sg/NSoE-MSS-CS">NSOE MSS-CS</a>: This project is developed as a part of the NSOE MSS-CS hosted in the School of Computing and Information System at Singapore Management University</li>
<li><a href ="https://github.com/android-hacker/VirtualXposed">VirtualXposed</a>: Powerful framework allowing users to use Xposed modules without root</li>

