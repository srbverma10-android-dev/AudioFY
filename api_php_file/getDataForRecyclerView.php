<?php
require "DataBase.php";

$db = new DataBase();
if ($db->dbConnect()) {
    $array = array();
    for( $i = 2 ; $i < 5 ; $i++ ){
        $data = new dataForRecyclerView();
        $data = $db->getDataForRecyclerView($i);
        array_push($array, $data);
    }

    $toReturn = json_encode($array);
    print($toReturn);


} else echo "\nError: Database connection";

?>
