#add_task
curl --location --request POST 'http://localhost:5000/tasks/?plannedTerm=2025-10-10&taskName=task1' \
--header 'Accept-Language: ukr' \
--header 'Authorization: Basic YWRtaW46MTExMQ=='
curl --location --request POST 'http://localhost:5000/tasks/?plannedTerm=2025-10-10&taskName=task2' \
--header 'Accept-Language: ukr' \
--header 'Authorization: Basic YWRtaW46MTExMQ=='
curl --location --request POST 'http://localhost:5000/tasks/?plannedTerm=2025-10-10&taskName=task3' \
--header 'Accept-Language: ukr' \
--header 'Authorization: Basic YWRtaW46MTExMQ=='
curl --location --request POST 'http://localhost:5000/tasks/?plannedTerm=2025-10-10&taskName=task4' \
--header 'Accept-Language: ukr' \
--header 'Authorization: Basic YWRtaW46MTExMQ=='
curl --location --request POST 'http://localhost:5000/tasks/?plannedTerm=2025-10-10&taskName=task5' \
--header 'Accept-Language: ukr' \
--header 'Authorization: Basic YWRtaW46MTExMQ=='
#get_all_tasks
curl --location --request GET 'http://localhost:5000/tasks/' \
--header 'Authorization: Basic dXNlcjowMDAw'
#change_task 2
curl --location --request PUT 'http://localhost:5000/tasks/change_task/2?plannedTerm=9999-10-10&taskName=taskVIP' \
--header 'Accept-Language: ukr' \
--header 'Authorization: Basic YWRtaW46MTExMQ=='
#del_task 3
curl --location --request DELETE 'http://localhost:5000/tasks/3' \
--header 'Accept-Language: ukr' \
--header 'Authorization: Basic YWRtaW46MTExMQ=='
#get_task 2
curl --location --request GET 'http://localhost:5000/tasks/2' \
--header 'Accept-Language: ukr' \
--header 'Authorization: Basic dXNlcjowMDAw'
#change_status 4
curl --location --request PUT 'http://localhost:5000/tasks/put_next_status/4' \
--header 'Accept-Language: ukr' \
--header 'Authorization: Basic YWRtaW46MTExMQ=='
#cancell_task 5
curl --location --request PUT 'http://localhost:5000/tasks/put_status_cancelled/5' \
--header 'Accept-Language: ukr' \
--header 'Authorization: Basic YWRtaW46MTExMQ=='