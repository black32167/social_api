# Collaboration APIs specifications
See also [generated documentation](https://black32167.github.io/social_api/docs/) for details.

## Administrative API
Backdoor API enabling services reset.
Eases black box testing of dependent service in the [mocked](../server-mocks) environment.

## User management API
User management API. Provides users registration and authentication for other services.

## Message management API
Allows registered users to send and receive messages.

## Task management API
Enables basic tasks management. 
Each task has a title, description and might have an assigned person responsible for task's fulfillment.
Once task is submitted, message is sent to assignee using message service.

