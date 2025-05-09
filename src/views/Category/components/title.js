import { onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'
import { onBeforeRouteUpdate } from 'vue-router'
import { getCategoryApi } from "@/apis/goods";

export function getTitle() {
    const categorytitle = ref("");
    const categorys = ref([]);
    const route = useRoute();

    const getCategoryName = async (id = route.params.id) => {
        const result = await getCategoryApi();
        categorys.value = result.data; // 正确赋值
        const foundCategory = categorys.value.find( // 用 .value 访问响应式数据
            item => item.id === parseInt(id)
        );
        if (foundCategory) {
            categorytitle.value = foundCategory.categoryname;
        }
    };

    onMounted(() => getCategoryName()); // 导入生命周期函数
    onBeforeRouteUpdate((to) => {
        getCategoryName(to.params.id);
    });
    return { categorytitle };
}