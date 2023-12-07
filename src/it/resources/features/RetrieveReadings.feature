@readings @retrieve_readings
Feature: Fetch Customer Meter Readings

  Background:
    Given Database contains the following data:
      | account_id | reading_type | meter_id | reading | date       |
      | 12349876   | GAS          | 22       | 167543  | 2023-06-03 |
      | 87653456   | ELECTRICITY  | 99       | 7865    | 2023-06-09 |
      | 12349876   | ELECTRICITY  | 44       | 8974    | 2023-06-08 |
      | 87653456   | GAS          | 55       | 8721    | 2023-06-05 |
      | 12349876   | GAS          | 22       | 167612  | 2023-07-05 |
      | 12349876   | ELECTRICITY  | 44       | 8992    | 2023-07-15 |


  Scenario: Fetching Meter Reading For Valid Account Number
    When Endpoint is called for retrieving all meter readings for Account Number "12349876"
    Then Response Status should be "OK"
    And Response Body should be
    """
    {
      "accountId": 12349876,
      "gasReadings": [
          {"id": 1, "meterId": 22, "reading": 167543, "date": "2023-06-03"},
          {"id": 5, "meterId": 22, "reading": 167612, "date": "2023-07-05"}
       ],
      "elecReadings": [
          {"id": 3, "meterId": 44, "reading": 8974, "date": "2023-06-08"},
          {"id": 6, "meterId": 44, "reading": 8992, "date": "2023-07-15"}
       ]
    }
    """

  Scenario: Fetching Meter Readings, No Data Found
    When Endpoint is called for retrieving all meter readings for Account Number "909090"
    Then Response Status should be "NO_CONTENT"

