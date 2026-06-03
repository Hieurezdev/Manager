import sys, re
from datetime import datetime, timedelta

file_path = '/home/hungdvlper/Documents/NhapMonCNPM/Manager/src/main/java/com/example/manager/data.sql'

with open(file_path, 'r') as f:
    lines = f.readlines()

# 1. Parse ChiTietHanhTrinh
ctht_dict = {}
in_ctht = False
for line in lines:
    if line.startswith('INSERT INTO ChiTietHanhTrinh'):
        in_ctht = True
        continue
    if in_ctht and line.startswith('INSERT INTO '):
        in_ctht = False
    
    if in_ctht and re.match(r'^\(', line):
        parts = line.strip().strip(',;').strip('()').split(',')
        if len(parts) >= 4:
            thuTuGa = int(parts[1].strip())
            nhaGaId = int(parts[2].strip())
            hanhTrinhId = int(parts[3].strip())
            if hanhTrinhId not in ctht_dict:
                ctht_dict[hanhTrinhId] = []
            ctht_dict[hanhTrinhId].append((thuTuGa, nhaGaId))

for ht in ctht_dict:
    ctht_dict[ht].sort()

# 2. Parse LichTrinh
lich_trinh_dict = {}
in_lt = False
lt_id = 1
for line in lines:
    if line.startswith('INSERT INTO LichTrinh'):
        in_lt = True
        continue
    if in_lt and line.startswith('INSERT INTO '):
        in_lt = False
        
    if in_lt and re.match(r'^\(', line):
        parts = line.strip().strip(',;').strip('()').split(',')
        if len(parts) >= 6:
            ngayKhoiHanh = parts[1].strip().strip("'")
            hanhTrinhId = int(parts[4].strip())
            lich_trinh_dict[lt_id] = (ngayKhoiHanh, hanhTrinhId)
            lt_id += 1

# 3. Generate exactly 10 records
new_records = []
count = 0
for ltid in range(24, 100):
    if count == 10: break
    if ltid == 35: continue
    if ltid not in lich_trinh_dict: continue
    
    ngayKhoiHanh_str, hanhTrinhId = lich_trinh_dict[ltid]
    if hanhTrinhId not in ctht_dict or not ctht_dict[hanhTrinhId]: continue
    
    ga_list = ctht_dict[hanhTrinhId]
    
    if count + len(ga_list) <= 10:
        dt = datetime.strptime(ngayKhoiHanh_str, '%Y-%m-%d %H:%M:%S')
        current_dt = dt
        
        for idx, (thuTuGa, nhaGaId) in enumerate(ga_list):
            if idx == 0:
                gioDen = "NULL"
                gioDi = f"'{current_dt.strftime('%Y-%m-%d %H:%M:%S')}'"
            elif idx == len(ga_list) - 1:
                # Travel time 2 hours
                current_dt += timedelta(hours=2)
                gioDen = f"'{current_dt.strftime('%Y-%m-%d %H:%M:%S')}'"
                gioDi = "NULL"
            else:
                # Travel time 2 hours
                current_dt += timedelta(hours=2)
                gioDen = f"'{current_dt.strftime('%Y-%m-%d %H:%M:%S')}'"
                # Stop time 10 mins
                current_dt += timedelta(minutes=10)
                gioDi = f"'{current_dt.strftime('%Y-%m-%d %H:%M:%S')}'"
            
            rec = f"('CTLT_{ltid:02d}_{idx+1:02d}', {gioDen}, {gioDi}, {nhaGaId}, {ltid})"
            new_records.append(rec)
        count += len(ga_list)

# 4. Insert into file
for i in range(len(lines)):
    if "'CTLT_23_02'" in lines[i] and "23" in lines[i]:
        if ';' in lines[i]:
            lines[i] = lines[i].replace(';', ',', 1)
        insert_idx = i + 1
        for j, rec in enumerate(new_records):
            terminator = ';' if j == len(new_records)-1 else ','
            lines.insert(insert_idx + j, rec + terminator + "\n")
        break

with open(file_path, 'w') as f:
    f.writelines(lines)
