# Collaboration APIs specifications

## Administrative API
Backdoor API enabling blackbox tests restart/'cleanup' services.
Eases blackbox testing of service in mocked environment.

## User management API
User management API. Enables users registration and authentication from other services.

## Message management API
Provides endpoints for registered users to send and receive messages.

## Task management API
Supports CRUD operation on tasks and notification.
Each task has title, description and might have an assigned person responsible for task's fulfillment.
Once task is submitted, message to assignee is sent using message service.

