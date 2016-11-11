if "%DIR%"=="" set DIR=image
if "%FORMAT%"=="" set FORMAT=jpg


if not exist %DIR%\*.%FORMAT% goto end


set TIME2=%time: =0%
set TIME2=%time:.=-%
set BACKUP_DIR=%DIR%_%DATE:/=-%-%TIME2::=-%

mkdir %BACKUP_DIR%
move %DIR%\*.%FORMAT% %BACKUP_DIR%


:end
