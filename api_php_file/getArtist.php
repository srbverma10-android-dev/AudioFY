<?php
require "DataBase.php";
$db = new DataBase();
if (isset($_POST['id'])) {

    if ($db->dbConnect()) {
        $artistId = $db->getArtistId($_POST['id']);
        if($artistId != null){
            print(json_encode($artistId));
        }
    } else echo "Error: Database connection";

} else echo "All fields are required";



?>