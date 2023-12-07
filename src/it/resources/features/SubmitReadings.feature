@readings @submit_readings
Feature: Submit Customer Readings

  Background:
    Given Database contains the following data:
      | account_id | reading_type | meter_id | reading | date       |
      | 12349876   | GAS          | 22       | 167612  | 2023-07-05 |
      | 12349876   | ELECTRICITY  | 44       | 8992    | 2023-07-15 |
      | 87653456   | ELECTRICITY  | 99       | 7865    | 2023-06-09 |
      | 87653456   | GAS          | 55       | 8721    | 2023-06-05 |

  Scenario: Customer Submits Gas and Electric Readings
    Given Customer with Account Number "12349876"
    And has gas reading
      | meterId | 22         |
      | reading | 167640     |
      | date    | 2023-07-18 |
    And has electricity reading
      | meterId | 44         |
      | reading | 8999       |
      | date    | 2023-07-18 |
    When Customer submits readings
    Then Response Status should be "ACCEPTED"
    And Database should now contain the following data
      | account_id | reading_type | meter_id | reading | date       |
      | 12349876   | GAS          | 22       | 167612  | 2023-07-05 |
      | 12349876   | ELECTRICITY  | 44       | 8992    | 2023-07-15 |
      | 87653456   | ELECTRICITY  | 99       | 7865    | 2023-06-09 |
      | 87653456   | GAS          | 55       | 8721    | 2023-06-05 |
      | 12349876   | GAS          | 22       | 167640  | 2023-07-18 |
      | 12349876   | ELECTRICITY  | 44       | 8999    | 2023-07-18 |

  Scenario: Customer Submits Gas Reading
    Given Customer with Account Number "12349876"
    And has gas reading
      | meterId | 22         |
      | reading | 167640     |
      | date    | 2023-07-18 |
    When Customer submits readings
    Then Response Status should be "ACCEPTED"
    And Database should now contain the following data
      | account_id | reading_type | meter_id | reading | date       |
      | 12349876   | GAS          | 22       | 167612  | 2023-07-05 |
      | 12349876   | ELECTRICITY  | 44       | 8992    | 2023-07-15 |
      | 87653456   | ELECTRICITY  | 99       | 7865    | 2023-06-09 |
      | 87653456   | GAS          | 55       | 8721    | 2023-06-05 |
      | 12349876   | GAS          | 22       | 167640  | 2023-07-18 |

  Scenario: Customer Submits Electric Reading
    Given Customer with Account Number "12349876"
    And has electricity reading
      | meterId | 44         |
      | reading | 8999       |
      | date    | 2023-07-18 |
    When Customer submits readings
    Then Response Status should be "ACCEPTED"
    And Database should now contain the following data
      | account_id | reading_type | meter_id | reading | date       |
      | 12349876   | GAS          | 22       | 167612  | 2023-07-05 |
      | 12349876   | ELECTRICITY  | 44       | 8992    | 2023-07-15 |
      | 87653456   | ELECTRICITY  | 99       | 7865    | 2023-06-09 |
      | 87653456   | GAS          | 55       | 8721    | 2023-06-05 |
      | 12349876   | ELECTRICITY  | 44       | 8999    | 2023-07-18 |

  Scenario: Customer Submits Electric Reading With invalid date
    Given Customer with Account Number "12349876"
    And has electricity reading
      | meterId | 44         |
      | reading | 8999       |
      | date    | 2023-07-14 |
    When Customer submits readings
    Then Response Status should be "BAD_REQUEST"
    And Database should now contain the following data
      | account_id | reading_type | meter_id | reading | date       |
      | 12349876   | GAS          | 22       | 167612  | 2023-07-05 |
      | 12349876   | ELECTRICITY  | 44       | 8992    | 2023-07-15 |
      | 87653456   | ELECTRICITY  | 99       | 7865    | 2023-06-09 |
      | 87653456   | GAS          | 55       | 8721    | 2023-06-05 |

  Scenario: Customer Submits Electric Reading With invalid reading
    Given Customer with Account Number "12349876"
    And has electricity reading
      | meterId | 44         |
      | reading | 8991       |
      | date    | 2023-07-16 |
    When Customer submits readings
    Then Response Status should be "BAD_REQUEST"
    And Database should now contain the following data
      | account_id | reading_type | meter_id | reading | date       |
      | 12349876   | GAS          | 22       | 167612  | 2023-07-05 |
      | 12349876   | ELECTRICITY  | 44       | 8992    | 2023-07-15 |
      | 87653456   | ELECTRICITY  | 99       | 7865    | 2023-06-09 |
      | 87653456   | GAS          | 55       | 8721    | 2023-06-05 |