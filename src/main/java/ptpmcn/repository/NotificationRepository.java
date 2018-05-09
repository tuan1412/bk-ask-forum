package ptpmcn.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ptpmcn.model.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long>{
	@Query("select noti from Notification noti where noti.user.id = :id order by noti.seen desc, noti.createAt desc")
	Page<Notification> findAllByUserId(@Param("id") Long id, Pageable pageable);
	
	@Query("select count(noti.id) from Notification noti where noti.user.id = :id and noti.seen = false")
	long countNotSeenNotification(@Param("id") Long id);
	
	@Modifying
	@Query("update Notification noti set noti.seen = true where noti.seen = false and noti.user.id =:id")
	int seenNotification(@Param("id") Long id);
}
