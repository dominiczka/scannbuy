<?php

$servername = "********";
$username = "********";
$password = "********";
$dbname = "********";

$conn = new mysqli($servername, $username, $password, $dbname);
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

$content = file_get_contents("php://input");
$decoded = json_decode($content, true);

$login = $decoded["LOGIN"];
$password = $decoded["HASLO"];
$phone = $decoded["NUMER_TELEFONU"];

if (empty($login) or empty($password) or empty($phone)) {
    $out = array(
        "result" => "invalid_data",
    );
    echo json_encode($out);
    die();
}

$sql = "INSERT INTO UZYTKOWNIK (LOGIN, HASLO, RABAT_PROCENT, NUMER_TELEFONU, UWAGI)
VALUES ('$login', '$password', 0, '$phone', '');";

if ($conn->query($sql) === TRUE) {
    $out = array(
        "result" => "success",
    );
}
else {
    $out = array(
        "result" => $conn->error,
    );
}

echo json_encode($out);

$conn->close();

?>
